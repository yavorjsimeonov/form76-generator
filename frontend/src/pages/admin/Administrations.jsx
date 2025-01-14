import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import { config } from "../../api/Constants"
import Header from "../../components/Header";
import Menu from "../../components/Menu";
import Footer from "../../components/Footer";
import AdministrationFormModal from "../../components/AdministrationForm";

function AdministrationsPage() {
    const [administrations, setAdministrations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const fetchAdministrations = async () => {
            try {
                const response = await fetch(`${config.API_BASE_URL}/administrations`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setAdministrations(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAdministrations();
    }, []);

    const handleCreate = async (newAdmin) => {
        try {
            const response = await fetch("${config.API_BASE_URL}/administrations", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(newAdmin),
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const createdAdmin = await response.json();
            setAdministrations([...administrations, createdAdmin]);
        } catch (error) {
            console.error("Error creating administration:", error);
        } finally {
            setShowModal(false);
        }
    };

    return (
        <div>
            <Header />
            <Menu activeKey="administrations" />
            <Container fluid="md">
                <Row className="justify-content-md-center">
                    <Col md={12}>
                        <h2>Administrations List</h2>
                        <Button
                            variant="primary"
                            onClick={() => setShowModal(true)}
                            style={{ marginBottom: "20px" }}
                        >
                            Create Administration
                        </Button>
                        {loading ? (
                            <p>Loading...</p>
                        ) : error ? (
                            <p>Error: {error}</p>
                        ) : administrations.length > 0 ? (
                            <Table striped bordered hover>
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Name</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                {administrations.map((administration) => (
                                    <tr key={administration.id}>
                                        <td>{administration.id}</td>
                                        <td>
                                            <Link to={`/administrations/${administration.id}`}>
                                                {administration.name}
                                            </Link>
                                        </td>
                                        <td>{administration.active ? "Active" : "Inactive"}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </Table>
                        ) : (
                            <p>No administrations found.</p>
                        )}
                    </Col>
                </Row>
            </Container>
            <Footer />


            <AdministrationFormModal
                show={showModal}
                onHide={() => setShowModal(false)}
                onSubmit={handleCreate}
                initialData={{ name: "", active: true }}
                title="Create Administration"
            />
        </div>
    );
}

export default AdministrationsPage;
