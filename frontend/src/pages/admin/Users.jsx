import React, { useState, useEffect } from "react";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import UserForm from "../../components/UserForm";
import { useAuth } from "../../components/common/AuthContext";
import { form76GeneratorApi } from "../../api/Form76GeneratorApi";

function UsersPage() {
    const Auth = useAuth();
    const user = Auth.getUser();

    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showUserModal, setShowUserModal] = useState(false); // Modal visibility state

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await form76GeneratorApi.getUsers(user); // Fetch all users
                setUsers(response.data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    const handleCreateUser = async (newUser) => {
        try {
            const response = await form76GeneratorApi.createUser(user, newUser); // Create a new user
            setUsers([...users, response.data]); // Add the new user to the list
        } catch (error) {
            console.error("Error creating user:", error);
        } finally {
            setShowUserModal(false);
        }
    };

    return (
        <div>
            <Header />
            <Menu activeKey="users" />
            <Container fluid="md">
                <Row className="justify-content-md-center">
                    <Col md={12}>
                        <h2>User Management</h2>
                        <Button
                            variant="primary"
                            onClick={() => setShowUserModal(true)} // Open Create User Modal
                            style={{ marginBottom: "20px" }}
                        >
                            Add User
                        </Button>
                        {loading ? (
                            <p>Loading...</p>
                        ) : error ? (
                            <p>Error: {error}</p>
                        ) : users.length > 0 ? (
                            <Table striped bordered hover>
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Email</th>
                                    <th>Username</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                {users.map((user) => (
                                    <tr key={user.id}>
                                        <td>{user.id}</td>
                                        <td>{user.firstName}</td>
                                        <td>{user.lastName}</td>
                                        <td>{user.email}</td>
                                        <td>{user.username}</td>
                                        <td>{user.role}</td>
                                        <td>{user.active ? "Active" : "Inactive"}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </Table>
                        ) : (
                            <p>No users found.</p>
                        )}
                    </Col>
                </Row>
            </Container>
            <Footer />

            <UserForm
                show={showUserModal}
                onHide={() => setShowUserModal(false)}
                onSubmit={handleCreateUser}
                initialData={[]}
                title="Add User"
            />
        </div>
    );
}

export default UsersPage;