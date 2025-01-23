import React, { useState } from 'react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Nav from 'react-bootstrap/Nav';

import { useAuth } from './AuthContext.jsx';

function Menu({ activeKey }) {
    const Auth = useAuth();

    return (
        <Container fluid="md" className="menu">
            <Row className="justify-content-md-center">
                <Col md={8}>
                    <Nav defaultActiveKey={activeKey} as="ul">
                        <Nav.Item as="li">
                            <Nav.Link href="/home" eventKey="home">Home</Nav.Link>
                        </Nav.Item>
                        <Nav.Item as="li">
                            <Nav.Link href="/administrations" eventKey="administrations">Administrations</Nav.Link>
                        </Nav.Item>
                        <Nav.Item as="li">
                            <Nav.Link href="/reports" eventKey="reports">Reports</Nav.Link>
                        </Nav.Item>
                        { Auth.getUser().role === 'ADMIN' &&
                            <Nav.Item as="li">
                                <Nav.Link href="/users" eventKey="users">Users</Nav.Link>
                            </Nav.Item>
                        }
                    </Nav>
                </Col>
            </Row>
        </Container>
    );
}

export default Menu;