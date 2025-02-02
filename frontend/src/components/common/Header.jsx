import React, { useState } from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Dropdown from 'react-bootstrap/Dropdown';
import Image from 'react-bootstrap/Image';
import { useAuth } from './AuthContext.jsx';
import "./Header.css";
import ChangePasswordForm from "./ChangePasswordForm";
import profilePlaceholder from "../../assets/blank-pfp.png";

function Header(props) {
    const Auth = useAuth();
    const user = Auth.getUser();
    const [showChangePasswordModal, setShowChangePasswordModal] = useState(false);

    console.log("Current User Object:", user); // Debugging

    const handleLogout = () => {
        Auth.userLogout();
    };

    return (
        <Container fluid className="header">
            <Row>
                <Col md={12}>
                    <Container fluid="md">
                        <Row className="justify-content-between align-items-center">
                            <Col md={6} className="logo">Форма 76</Col>
                            <Col md={6} className="userinfo d-flex justify-content-end">
                                {user !== null && user.username && (
                                    <Dropdown align="end">
                                        <Dropdown.Toggle as="div" id="profile-dropdown" className="d-flex align-items-center">
                                            <Image
                                                src={profilePlaceholder}
                                                alt="Profile"
                                                roundedCircle
                                                width="40"
                                                height="40"
                                                style={{ cursor: "pointer", marginRight: "10px" }}
                                            />
                                            <span>{user.username}</span>
                                        </Dropdown.Toggle>
                                        <Dropdown.Menu>
                                            <Dropdown.Item onClick={() => setShowChangePasswordModal(true)}>
                                                Change Password
                                            </Dropdown.Item>
                                            <Dropdown.Divider />
                                            <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                )}
                            </Col>
                        </Row>
                    </Container>
                </Col>
            </Row>

            {/* Change Password Modal */}
            <ChangePasswordForm
                show={showChangePasswordModal}
                onHide={() => setShowChangePasswordModal(false)}
            />
        </Container>
    );
}

export default Header;
