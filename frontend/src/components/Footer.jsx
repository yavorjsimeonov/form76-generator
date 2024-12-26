import React from 'react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import "./Footer.css";

function Footer() {
    return (

        <Container fluid className="footer">
            <Row className="justify-content-md-center">
                <Col md={2} className="">© 2024</Col>
            </Row>
        </Container>
    );
}


export default Footer;