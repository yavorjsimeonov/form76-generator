import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import { form76GeneratorApi } from '../api/Form76GeneratorApi.js';
import { useAuth } from './AuthContext.jsx';
import { logError } from './ErrorHanlder.jsx';

function LogInForm() {
    const Auth = useAuth();
    const navigate = useNavigate();

    const isLoggedIn = Auth.userIsAuthenticated();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isError, setIsError] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        if (isLoggedIn) {
            navigate("/home");
        }
    }, [isLoggedIn, navigate]);

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
        setIsError(false); // Reset error on input change
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
        setIsError(false); // Reset error on input change
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!(username && password)) {
            setIsError(true);
            setErrorMessage("Please provide both username and password.");
            return;
        }

        try {
            const response = await form76GeneratorApi.authenticate(username, password);

            const { id, name, email, role } = response.data;
            const authdata = window.btoa(username + ':' + password); // Consider using a token for security
            const authenticatedUser = { id, username, name, email, role, authdata };

            Auth.userLogin(authenticatedUser);
            setUsername('');
            setPassword('');
            setIsError(false);
            navigate("/home");
        } catch (error) {
            logError(error);
            if (error.response && error.response.status === 401) {
                setErrorMessage("Incorrect username or password.");
            } else {
                setErrorMessage("An error occurred. Please try again.");
            }
            setIsError(true);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="login-form">
            <Container fluid="md">
                <Row className="justify-content-md-center">
                    <Col md={4}>
                        {isError && <Alert variant="warning">{errorMessage}</Alert>}
                    </Col>
                </Row>
                <Row className="justify-content-md-center">
                    <Col md={1}>Username:</Col>
                    <Col md={3}>
                        <input
                            type="text"
                            id="username"
                            className="form-control"
                            placeholder="Username"
                            value={username}
                            onChange={handleUsernameChange}
                            required
                        />
                    </Col>
                </Row>
                <Row className="justify-content-md-center">
                    <Col md={1}>Password:</Col>
                    <Col md={3}>
                        <input
                            type="password"
                            id="password"
                            className="form-control"
                            placeholder="Password"
                            value={password}
                            onChange={handlePasswordChange}
                            required
                        />
                    </Col>
                </Row>
                <Row className="justify-content-md-center">
                    <Col md={3} className="text-right">
                        <Button variant="primary" type="submit">Login</Button>
                    </Col>
                </Row>
            </Container>
        </form>
    );
}

export default LogInForm;