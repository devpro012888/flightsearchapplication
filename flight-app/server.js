const express = require('express');
const fs = require('fs');
const xml2js = require('xml2js');
const bodyParser = require('body-parser');
const cors = require('cors');
const bcrypt = require('bcrypt');
const { body, validationResult } = require('express-validator');

const app = express();
const PORT = 3000;
const USERS_XML_PATH = './users.xml';

app.use(cors());
app.use(bodyParser.json());

// Helper: Read users.xml and convert to JSON
function readUsersXml(callback) {
    fs.readFile(USERS_XML_PATH, (err, data) => {
        if (err) return callback(err);
        xml2js.parseString(data, (err, result) => {
            if (err) return callback(err);
            callback(null, result);
        });
    });
}

// Helper: Write JSON back to users.xml
function writeUsersXml(usersJson, callback) {
    const builder = new xml2js.Builder();
    const xml = builder.buildObject(usersJson);
    fs.writeFile(USERS_XML_PATH, xml, callback);
}

// POST /login
app.post('/login', (req, res) => {
    const { username, password } = req.body;
    readUsersXml((err, result) => {
        if (err) return res.status(500).send('Server error');

        const users = result.users.user || [];
        const user = users.find(u => u.username[0] === username);

        if (user) {
            bcrypt.compare(password, user.password[0], (err, isMatch) => {
                if (err) return res.status(500).send('Error verifying password');

                if (isMatch) {
                    res.status(200).send({ success: true, message: 'Login successful' });
                } else {
                    res.status(401).send({ success: false, message: 'Invalid credentials' });
                }
            });
        } else {
            res.status(401).send({ success: false, message: 'Invalid credentials' });
        }
    });
});

// POST /register with input validation and password hashing
app.post('/register',
    [
        body('username').isLength({ min: 3 }).withMessage('Username must be at least 3 characters long'),
        body('password').isLength({ min: 6 }).withMessage('Password must be at least 6 characters long')
    ],
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({ success: false, errors: errors.array() });
        }

        const { username, password } = req.body;
        readUsersXml((err, result) => {
            if (err) return res.status(500).send('Server error');

            const users = result.users.user || [];
            const exists = users.some(u => u.username[0] === username);

            if (exists) {
                return res.status(409).send({ success: false, message: 'User already exists' });
            }

            bcrypt.hash(password, 10, (err, hashedPassword) => {
                if (err) return res.status(500).send('Error hashing password');

                users.push({ username: [username], password: [hashedPassword] });
                result.users.user = users;

                writeUsersXml(result, err => {
                    if (err) return res.status(500).send('Error saving user');
                    res.status(201).send({ success: true, message: 'User registered' });
                });
            });
        });
    });

// GET /flights (static data for now)
app.get('/flights', (req, res) => {
    const flights = [
        { flightNumber: 'CG123', origin: 'NYC', destination: 'LAX', departure: '2025-06-01T10:00', arrival: '2025-06-01T13:00' },
        { flightNumber: 'CG456', origin: 'LAX', destination: 'CHI', departure: '2025-06-02T14:00', arrival: '2025-06-02T18:00' },
    ];
    res.status(200).json(flights);
});

app.listen(PORT, () => {
    console.log(`Flight server running on http://localhost:${PORT}`);
});
