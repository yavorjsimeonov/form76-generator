import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Row, Col, Button } from "react-bootstrap";
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import AdministrationFormModal from "../../components/AdministrationForm";
import CreateLocationForm from "../../components/CreateLocationForm";
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
    const [showAdminModal, setShowAdminModal] = useState(false);
    const [showLocationModal, setShowLocationModal] = useState(false);

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
            setShowAdminModal(false);
        }
    };

    const handleCreateLocation = async (newLocation) => {
        try {
            const response = await form76GeneratorApi.createLocation(user, {
                ...newLocation,
                administrationId: id,
            });
            setAdministration((prev) => ({
                ...prev,
                locations: [...prev.locations, response.data],
            }));
        } catch (error) {
            console.error("Error creating location:", error);
        } finally {
            setShowLocationModal(false);
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

                                <LocationList locations={administration.locations} />

                                <Button
                                    variant="primary"
                                    onClick={() => setShowLocationModal(true)}
                                    style={{ marginRight: "10px"}}
                                >
                                    Create Location
                                </Button>
                                <Button
                                    variant="warning"
                                    onClick={() => setShowAdminModal(true)}
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

            {/* Modal for Editing Administration */}
            <AdministrationFormModal
                show={showAdminModal}
                onHide={() => setShowAdminModal(false)}
                onSubmit={handleEdit}
                initialData={administration || { name: "", active: true }}
                title="Edit Administration"
            />

            {/* Modal for Creating Location */}
            <CreateLocationForm
                show={showLocationModal}
                onHide={() => setShowLocationModal(false)}
                onSubmit={handleCreateLocation}
                initialData={[]}
                title="Create Location"
            />
        </div>
    );
}

export default AdministrationPage;
