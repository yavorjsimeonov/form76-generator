import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';

import { form76GeneratorApi } from '../api/Form76GeneratorApi.js';
import { useAuth } from './AuthContext.jsx';
import { logError }  from './ErrorHanlder.jsx';


function LogInForm() {
    const Auth = useAuth();
    const navigate = useNavigate();

    const isLoggedIn = Auth.userIsAuthenticated();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isError, setIsError] = useState(false);

    const [data, setData] = useState(null);

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (!(username && password)) {
            setIsError(true)
            return
        }

        try {
            const response = await form76GeneratorApi.authenticate(username, password)
            const { id, name, role } = response.data
            const authdata = window.btoa(username + ':' + password)
            const authenticatedUser = { id, name, role, authdata }

            Auth.userLogin(authenticatedUser)

            setUsername('')
            setPassword('')
            setIsError(false)
            navigate("/home");
        } catch (error) {
            logError(error)
            setIsError(true)
        }
    }

    return isLoggedIn ? navigate("/home") : (
        <form onSubmit={handleSubmit} className="login-form">
            <Container fluid="md">
                <Row className="justify-content-md-center">
                    <Col md={4}>{isError && <Alert key="warning" variant="warning">The username or password provided are incorrect!</Alert>}</Col>
                </Row>
                <Row className="justify-content-md-center">
                    <Col md={1}>Username:</Col>
                    <Col md={3}>
                        <input type="text" id="username" className="form-control" placeholder="Username" aria-label="Username"
                               aria-describedby="basic-addon1" value={username} onChange={handleUsernameChange} required></input>
                    </Col>
                </Row>
                <Row className="justify-content-md-center">
                    <Col md={1}>Password:</Col>
                    <Col md={3}>
                        <input type="password" id="password" className="form-control" placeholder="Passwod" aria-label="Password"
                               aria-describedby="basic-addon1" value={password} onChange={handlePasswordChange} required></input>
                    </Col>
                </Row>
                <Row className="justify-content-md-center">
                    <Col md={1}></Col>
                    <Col md={3} className="right">
                        <Button variant="primary" type="submit">Login</Button>
                    </Col>
                </Row>
            </Container>
        </form>
    );
}

export default LogInForm;