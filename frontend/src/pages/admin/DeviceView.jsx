import React from 'react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import Header from "../../components/Header";
import Menu from "../../components/Menu";
import Footer from "../../components/Footer";

function DeviceViewPage() {
    return (
        <div className="">
            <Header/>
            <Menu activeKey="devices"/>
            <Container fluid="md" className="">
                <Row className="justify-content-md-center">
                    <Col md={12}>TODO: Show Device details by deviceId</Col>
                </Row>
            </Container>

            <Footer />
        </div>
    );
}

export default DeviceViewPage;