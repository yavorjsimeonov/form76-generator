import React, { useState } from 'react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Nav from 'react-bootstrap/Nav';
import './Menu.css';

import { useAuth } from './AuthContext.jsx';

function LeftMenu({ activeKey }) {
    const Auth = useAuth();

    return (
        <Container>
            <Nav className="d-none d-md-block bg-light sidebar" defaultActiveKey={activeKey} activeKey={activeKey} as="div" navbar={true}>
                <div className="nav-bar-logo">
                    <img src="/form76generatorlogo-small.png"/>
                </div>
                <Nav.Item as="div" className="mt-3">
                    <Nav.Link href="/home" eventKey="home">Начало</Nav.Link>
                </Nav.Item>
                <Nav.Item as="div">
                    <Nav.Link href="/administrations" eventKey="administrations">Администрации</Nav.Link>
                </Nav.Item>
                <Nav.Item as="div">
                    <Nav.Link href="/reports" eventKey="reports">Справки</Nav.Link>
                </Nav.Item>
                { Auth.getUser().role === 'ADMIN' &&
                    <Nav.Item as="li">
                        <Nav.Link href="/users" eventKey="users">Потребители</Nav.Link>
                    </Nav.Item>
                }
            </Nav>
        </Container>
    );
}

export default LeftMenu;