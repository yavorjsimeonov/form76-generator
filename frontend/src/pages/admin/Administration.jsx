import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Row, Col, Button } from "react-bootstrap";
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import AdministrationFormModal from "../../components/AdministrationForm";
import LocationList from "../../components/LocationList";
import { useAuth } from "../../components/common/AuthContext";
import { form76GeneratorApi } from "../../api/Form76GeneratorApi";

function AdministrationPage() {
    const Auth = useAuth();
    const user = Auth.getUser();

    const { id } = useParams();
    const navigate = useNavigate();
    const [administration, setAdministration] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showAdministrationModal, setShowAdministrationModal] = useState(false);

    useEffect(() => {
        const fetchAdministration = async () => {
            try {
                const response = await form76GeneratorApi.getAdministration(user, id);
                setAdministration(response.data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAdministration();
    }, [id]);

    const handleEdit = async (updatedAdministration) => {
        try {
            const response = await form76GeneratorApi.updateAdministration(user, id, updatedAdministration);
            setAdministration(response.data);
        } catch (error) {
            console.error("Error editing administration:", error);
        } finally {
            setShowAdministrationModal(false);
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
                                <div className="text-start">
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

                                <Button
                                    variant="warning"
                                    onClick={() => setShowAdministrationModal(true)}
                                    style={{ marginRight: "10px" }}
                                >
                                    Edit
                                </Button>
                                <Button variant="secondary" onClick={() => navigate(-1)}>
                                    Back
                                </Button>

                                {/* Locations List */}
                                <LocationList
                                    locations={administration.locations}
                                    administrationId={id} // Pass the administration ID
                                    onLocationCreated={(newLocation) =>
                                        setAdministration((prev) => ({
                                            ...prev,
                                            locations: [...prev.locations, newLocation],
                                        }))
                                    }
                                />
                            </>
                        ) : (
                            <p>Administration not found.</p>
                        )}
                    </Col>
                </Row>
            </Container>
            <Footer />

            {/* Modal for Editing Administration */}
            <AdministrationFormModal
                show={showAdministrationModal}
                onHide={() => setShowAdministrationModal(false)}
                onSubmit={handleEdit}
                initialData={administration || { name: "", active: true }}
                title="Edit Administration"
            />
        </div>
    );
}

export default AdministrationPage;
