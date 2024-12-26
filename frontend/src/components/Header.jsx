import React, {useState} from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import { useAuth } from './AuthContext.jsx';
import "./Header.css";

function Header(props) {
    const Auth = useAuth();
    const [state, setState] = useState()

    const handleLogout = () => {
        Auth.userLogout();
    };

    return (

        <Container fluid className="header">
            <Row className="">
                <Col md={12}>
                    <Container fluid="md">
                        <Row className="justify-content-md-center ">
                            <Col md={6} className="logo">Форма 76</Col>
                            <Col md={6} className="userinfo">
                                { Auth.getUser() !==null &&
                                <spn>Username: {Auth.getUser().name}, <a href="/logout" onClick={handleLogout}>Logout</a> </spn>
                                }
                            </Col>
                        </Row>
                    </Container>
                </Col>
            </Row>
        </Container>


    );
}

export default Header;
