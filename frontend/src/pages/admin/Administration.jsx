import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Row, Col, Button, Table } from "react-bootstrap";
import Header from "../../components/Header";
import Menu from "../../components/Menu";
import Footer from "../../components/Footer";
import AdministrationFormModal from "../../components/AdministrationForm";
import LocationList from "../../components/LocationList";
import {config} from "../../api/Constants";

function AdministrationPage() {
    const { id } = useParams();

    const navigate = useNavigate();
    const [administration, setAdministration] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const fetchAdministration = async () => {
            try {
                const response = await fetch(`${config.API_BASE_URL}/administrations/${id}`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setAdministration(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAdministration();
    }, [id]);

    const handleEdit = async (updatedAdmin) => {
        try {
            const response = await fetch(`${config.API_BASE_URL}/api/administrations/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(updatedAdmin),
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const editedAdmin = await response.json();
            setAdministration(editedAdmin);
        } catch (error) {
            console.error("Error editing administration:", error);
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
                        {loading ? (
                            <p>Loading...</p>
                        ) : error ? (
                            <p>Error: {error}</p>
                        ) : administration ? (
                            <>
                                <h2>Administration Details</h2>
                                <div class="text-start">
                                    <div>
                                        <label>ID: </label><span>{administration.id}</span>
                                    </div>
                                    <div>
                                        <label>Name: </label><span>{administration.name}</span>
                                    </div>
                                    <div>
                                        <label>Status: </label><span>{administration.active ? "Active" : "Inactive"}</span>
                                    </div>
                                </div>

                                <LocationList locations={administration.locations} />

                                <Button
                                    variant="warning"
                                    onClick={() => setShowModal(true)}
                                    style={{ marginRight: "10px" }}
                                >
                                    Edit
                                </Button>
                                <Button variant="secondary" onClick={() => navigate(-1)}>
                                    Back
                                </Button>
                            </>
                        ) : (
                            <p>Administration not found.</p>
                        )}
                    </Col>
                </Row>
            </Container>
            <Footer />

            <AdministrationFormModal
                show={showModal}
                onHide={() => setShowModal(false)}
                onSubmit={handleEdit}
                initialData={administration || { name: "", active: true }}
                title="Edit Administration"
            />
        </div>
    );
}

export default AdministrationPage;
