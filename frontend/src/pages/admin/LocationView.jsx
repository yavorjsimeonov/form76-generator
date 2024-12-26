import React from 'react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import Header from "../../components/Header";
import Menu from "../../components/Menu";
import Footer from "../../components/Footer";

function LocationViewPage() {
    return (
        <div className="">
            <Header/>
            <Menu activeKey="locations"/>
            <Container fluid="md" className="">
                <Row className="justify-content-md-center">
                    <Col md={12}>TODO: Show Location details by locationId</Col>
                </Row>
            </Container>

            <Footer />
        </div>
    );
}

export default LocationViewPage;