import React from 'react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import Header from "../../components/Header";
import Menu from "../../components/Menu";
import Footer from "../../components/Footer";

function DevicesPage() {
    return (
        <div className="">
            <Header/>
            <Menu activeKey="devices"/>
            <Container fluid="md" className="">
                <Row className="justify-content-md-center">
                    <Col md={12}>TODO: Show Devices list by locationId</Col>
                </Row>
            </Container>

            <Footer />
        </div>
    );
}

export default DevicesPage;