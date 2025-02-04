import React, { useState, useEffect } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import Card from 'react-bootstrap/Card';
import { form76GeneratorApi } from '../api/Form76GeneratorApi.js';
import { useAuth } from './common/AuthContext.jsx';
import { logError } from './common/ErrorHanlder.jsx';

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

            const { id, userName, firstName, lastName, email, role } = response.data;
            const authdata = window.btoa(username + ':' + password); // Consider using a token for security
            const authenticatedUser = { id, userName, firstName, lastName, email, role, authdata };

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
            <Card class="card justify-content-md-center shadow" style={{ width: '20rem' }}>
                <Card.Body>
                    <Card.Title>
                        <img src="./static/form76generatorlogo.png"/>
                    </Card.Title>
                        <Container fluid="sm">
                            <Row className="justify-content-md-center">
                                <Col>
                                    {isError && <Alert variant="warning">{errorMessage}</Alert>}
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center">
                                <Col>
                                    <label htmlFor="username" className="form-label">Потребителско име:</label>
                                    <input
                                        type="text"
                                        id="username"
                                        className="form-control"
                                        placeholder="Потребителско име"
                                        value={username}
                                        onChange={handleUsernameChange}
                                        required
                                    />
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center">
                                <Col>
                                    <label htmlFor="password" className="form-label">Парола</label>
                                    <input
                                        type="password"
                                        id="password"
                                        className="form-control"
                                        placeholder="Парола"
                                        value={password}
                                        onChange={handlePasswordChange}
                                        required
                                    />
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center">
                                <Col className="text-center">
                                    <Button variant="primary" type="submit">Вход</Button>
                                </Col>
                            </Row>
                        </Container>
                </Card.Body>
            </Card>



        </form>
    );
}

export default LogInForm;