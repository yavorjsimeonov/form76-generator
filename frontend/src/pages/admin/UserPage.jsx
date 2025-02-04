import React, { useState, useEffect } from "react";
import { Container, Row, Col, Table, Button, Card } from "react-bootstrap";
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import UserForm from "../../components/UserForm";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../../components/common/AuthContext";
import { form76GeneratorApi } from "../../api/Form76GeneratorApi";
import LeftMenu from "../../components/common/LeftMenu";

function UserDetailsPage() {
    const { id } = useParams();
    const Auth = useAuth();
    const user = Auth.getUser();
    const navigate = useNavigate();

    const [userDetails, setUserDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);

    useEffect(() => {
        const fetchUserDetails = async () => {
            try {
                const response = await form76GeneratorApi.getUserDetails(user, id);
                setUserDetails(response.data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchUserDetails();
    }, [id]);

    const handleEditUser = async (updatedUser) => {
        try {
            if (!updatedUser.password?.trim() || !updatedUser.confirmPassword?.trim()) {
                delete updatedUser.password;
                delete updatedUser.confirmPassword;
            } else {
                if (updatedUser.password !== updatedUser.confirmPassword) {
                    alert("Passwords do not match!");
                    return;
                }
            }

            const response = await form76GeneratorApi.updateUser(user, userDetails.id, updatedUser);
            setUserDetails(response.data);
        } catch (error) {
            console.error("Error updating user:", error);
            alert(error.response?.data?.message || "An error occurred while updating user details.");
        } finally {
            setShowEditModal(false);
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!userDetails) return <p>User not found.</p>;

    return (
        <div>
            <Container fluid>
                <Row>
                    <Col md={2} id="sidebar-wrapper">
                        <LeftMenu activeKey="users"/>
                    </Col>
                    <Col md={10} id="page-content-wrapper" >
                        <Header/>
                        <div styleName="clear: both;"></div>
                        <Container fluid="md">
                            <Row className="justify-content-md-center">
                                    <div>
                                        <h2>Потребител <span className="object">{userDetails.username}</span></h2>
                                    </div>
                                    <Card class="card">
                                        <Card.Title className="mt-3">
                                            Детайли
                                        </Card.Title>

                                        <Card.Body>
                                            <Container>
                                                <Row>
                                                    <Col md={6}>
                                                        <b>Потребителско име: </b>{userDetails.username} <br/>
                                                        <b>Собствено име: </b>{userDetails.firstName} <br/>
                                                        <b>Фамилия: </b>{userDetails.lastName} <br/>
                                                    </Col>
                                                    <Col md={6}>
                                                        <b>Електронна поща: </b>{userDetails.email} <br/>
                                                        <b>Роля: </b>{userDetails.role} <br/>
                                                        <b>Статус: </b>{userDetails.active ? "Active" : "Inactive"} <br/>
                                                    </Col>
                                                </Row>
                                            </Container>

                                        </Card.Body>
                                        <Card.Body>
                                            <Container>

                                                <Row>
                                                    <Col>
                                                        <Button
                                                            variant="warning"
                                                            onClick={() => setShowEditModal(true)}
                                                            style={{ marginRight: "10px" }}
                                                        >
                                                            Редакция
                                                        </Button>
                                                        <Button variant="secondary" onClick={() => navigate(-1)}>
                                                            Назад
                                                        </Button>
                                                    </Col>
                                                </Row>
                                            </Container>

                                        </Card.Body>
                                    </Card>

                            </Row>
                        </Container>
                        <Footer />
                    </Col>
                </Row>
            </Container>

            <UserForm
                show={showEditModal}
                onHide={() => setShowEditModal(false)}
                onSubmit={handleEditUser}
                initialData={{ ...userDetails, password: "", confirmPassword: "" }}
                title="Редакция на потребител"
            />
        </div>
    );
}

export default UserDetailsPage;
