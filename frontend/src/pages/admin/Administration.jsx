import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {Container, Row, Col, Button, Card} from "react-bootstrap";
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import AdministrationFormModal from "../../components/AdministrationForm";
import LocationList from "../../components/LocationList";
import { useAuth } from "../../components/common/AuthContext";
import { form76GeneratorApi } from "../../api/Form76GeneratorApi";
import LeftMenu from "../../components/common/LeftMenu";

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
        <>
            <Container fluid>
                <Row>
                    <Col md={2} id="sidebar-wrapper">
                        <LeftMenu activeKey="administrations"/>
                    </Col>
                    <Col md={10} id="page-content-wrapper" >
                        <Header/>
                        <Container fluid="md" className="">
                            <Row className="justify-content-md-center">
                                {loading ? (
                                    <p>Loading...</p>
                                ) : error ? (
                                    <p>Error: {error}</p>
                                ) : administration ? (
                                    <>
                                    <div>
                                        <h2>Администрация <span className="object">{administration.name}</span></h2>
                                    </div>
                                    <Card class="card">
                                        <Card.Title className="mt-3">Детайли</Card.Title>

                                        <Card.Body>
                                            <Container>
                                                <Row>
                                                    <Col md={6}>
                                                        <b>Име: </b>{administration.name} <br/>
                                                        <b>Статус: </b>{administration.active ? "Активен" : "Не активен"} <br/>
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
                                                            onClick={() => setShowAdministrationModal(true)}
                                                            style={{ marginRight: "10px" }}
                                                        >
                                                            Редакция
                                                        </Button>
                                                        <Button variant="secondary" onClick={() => navigate(-1)}>
                                                            Назад
                                                        </Button>



                                                        {/*<Button
                                                            variant="primary"
                                                            onClick={() => setShowReportModal(true)}
                                                            style={{ marginRight: "10px" }}
                                                        >
                                                            Добавяне на локация
                                                        </Button>*/}

                                                    </Col>
                                                </Row>
                                            </Container>

                                        </Card.Body>
                                    </Card>

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
                            </Row>
                        </Container>

                        <Footer />
                    </Col>
                </Row>
            </Container>
            <div>
                {/* Modal for Editing Administration */}
                <AdministrationFormModal
                    show={showAdministrationModal}
                    onHide={() => setShowAdministrationModal(false)}
                    onSubmit={handleEdit}
                    initialData={administration || { name: "", active: true }}
                    title="Edit Administration"
                />
            </div>

        </>







    );
}

export default AdministrationPage;
