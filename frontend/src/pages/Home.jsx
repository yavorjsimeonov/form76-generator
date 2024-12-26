import React from 'react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import Header from "../components/Header";
import Menu from "../components/Menu";
import Footer from "../components/Footer";

import { useAuth } from '../components/AuthContext.jsx';

function HomePage() {
    const Auth = useAuth();

    return (
        <div className="home-page">
            <Header/>
            <Menu activeKey="home"/>
            <Container fluid="md" className="welcome">
                <Row className="justify-content-md-center">
                    <Col md={4}> <h3>Welcome {Auth.getUser().name}</h3></Col>
                </Row>
            </Container>

            <Footer />
        </div>

    );
}

export default HomePage;