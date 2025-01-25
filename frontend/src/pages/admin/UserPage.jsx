import React, { useState, useEffect } from "react";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import UserForm from "../../components/UserForm";
import { useParams } from "react-router-dom";
import { useAuth } from "../../components/common/AuthContext";
import { form76GeneratorApi } from "../../api/Form76GeneratorApi";

function UserDetailsPage() {
    const { id } = useParams();
    const Auth = useAuth();
    const user = Auth.getUser();

    const [userDetails, setUserDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);

    useEffect(() => {
        const fetchUserDetails = async () => {
            try {
                const response = await form76GeneratorApi.getUserDetails(user, id); // Fetch user details
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
            const response = await form76GeneratorApi.updateUser(user, id, updatedUser);
            setUserDetails(response.data);
        } catch (error) {
            console.error("Error updating user:", error);
        } finally {
            setShowEditModal(false);
        }
    };

    const handleChangePassword = () => {
        // Implement password change logic or redirect to a password change page
        alert("Redirecting to password change...");
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!userDetails) return <p>User not found.</p>;

    return (
        <div>
            <Header />
            <Menu activeKey="users" />
            <Container>
                <Row>
                    <Col>
                        <h2>User Details</h2>
                        <Table striped bordered hover>
                            <tbody>
                            <tr>
                                <td>Username</td>
                                <td>{userDetails.username}</td>
                            </tr>
                            <tr>
                                <td>First Name</td>
                                <td>{userDetails.firstName}</td>
                            </tr>
                            <tr>
                                <td>Last Name</td>
                                <td>{userDetails.lastName}</td>
                            </tr>
                            <tr>
                                <td>Email</td>
                                <td>{userDetails.email}</td>
                            </tr>
                            <tr>
                                <td>Role</td>
                                <td>{userDetails.role}</td>
                            </tr>
                            <tr>
                                <td>Status</td>
                                <td>{userDetails.active ? "Active" : "Inactive"}</td>
                            </tr>
                            </tbody>
                        </Table>
                        <Button
                            variant="warning"
                            onClick={() => setShowEditModal(true)}
                            style={{ marginRight: "10px" }}
                        >
                            Edit User
                        </Button>
                        <Button variant="primary" onClick={handleChangePassword}>
                            Change Password
                        </Button>
                    </Col>
                </Row>
            </Container>
            <Footer />

            <UserForm
                show={showEditModal}
                onHide={() => setShowEditModal(false)}
                onSubmit={handleEditUser}
                initialData={userDetails}
                title="Edit User"
            />
        </div>
    );
}

export default UserDetailsPage;
