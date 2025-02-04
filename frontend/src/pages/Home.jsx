import React from 'react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import Header from "../components/common/Header";
import Menu from "../components/common/Menu";
import LeftMenu from "../components/common/LeftMenu";
import Footer from "../components/common/Footer";

import { useAuth } from '../components/common/AuthContext.jsx';

function HomePage() {
    const Auth = useAuth();
    const user = Auth.getUser();

    return (
        <Container fluid>
            <Row>
                <Col md={2} id="sidebar-wrapper">
                    <LeftMenu activeKey="home"/>
                </Col>
                <Col md={10} id="page-content-wrapper" >
                    <Header/>
                    <Container fluid="md" className="welcome">
                        <Row className="justify-content-md-center">
                            <Col md={4}> <h3>Здравейте, {user.firstName} {user.lastName}</h3></Col>
                        </Row>
                    </Container>
                    <Footer />
                </Col>
            </Row>
        </Container>
    );
}

export default HomePage;