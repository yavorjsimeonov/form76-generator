import React, { useState, useEffect } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";
import { Container, Row, Col, Table, Button, Card } from "react-bootstrap";
import { config } from "../../api/Constants"
import LocationForm from "../../components/LocationForm";
import GenerateReportModal from "../../components/GenerateReportForm";
import Footer from "../../components/common/Footer";
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Toast from "../../components/common/Toast";
import {useAuth} from "../../components/common/AuthContext";
import {form76GeneratorApi} from "../../api/Form76GeneratorApi";
import ReportList from "../../components/ReportList";
import LeftMenu from "../../components/common/LeftMenu";

function LocationDetailsPage() {
    const Auth = useAuth();
    const user = Auth.getUser();

    const { id } = useParams();
    const { state } = useLocation();
    const navigate = useNavigate();

    const [location, setLocation] = useState(/*state?.location || */null);
    const [loading, setLoading] = useState(/*!state?.locationId*/true);
    const [error, setError] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showReportModal, setShowReportModal] = useState(false);
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    const [toastColor, setToastColor] = useState("primary");

    useEffect(() => {
            const fetchLocation = async () => {
                try {
                    const response = await form76GeneratorApi.getLocation(user, id);
                    setLocation(response.data);
                } catch (error) {
                    setError(error.message);
                } finally {
                    setLoading(false);
                }
            };

            fetchLocation();
    }, [id]);

    const handleEdit = async (updatedLocation) => {
        try {
            const response = await form76GeneratorApi.updateLocation(user, id, updatedLocation);
            setLocation(response.data);
        } catch (error) {
            console.error("Error editing location:", error);
        } finally {
            setShowModal(false);
        }
    };

    const handleGenerateReport = async (reportRequest) => {
        const request = {
            locationId: location.id,
            ...reportRequest,
        };

        try {
            const response = await form76GeneratorApi.generateReportForLocation(user, location.id, request);
            console.log("Generate report response:", response);

            if (response.status === 200 && response.data) {
                setToastMessage(`Report generation triggered successfully! An email will be send to ${location.representativeEmail}`);
                setToastColor("success"); // Green toast for success
                setShowToast(true);
            }
        } catch (error) {
            console.error("Error when triggering report generation:", error);
            setToastMessage(`Error when triggering report generation for location ${location.name}`);
            setToastColor("danger");
            setShowToast(true);
        }


    };

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p>Error: {error}</p>;
    }

    if (!location) {
        return <p>Location not found.</p>;
    }

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
                                <div>
                                <h2>Локация <span className="object">{location.name}</span></h2>
                                </div>
                                <Card class="card">
                                    <Card.Title className="mt-3">Детайли</Card.Title>

                                    <Card.Body>
                                        <Container>
                                            <Row>
                                                <Col md={6}>
                                                    <b>Име: </b>{location.name} <br/>
                                                    <b>Външно Id: </b>{location.extCommunityId} <br/>
                                                    <b>Външно UUId: </b>{location.extCommunityUuid} <br/>
                                                    <b>Алгоритъм на справката: </b>{location.reportAlgorithm} <br/>
                                                </Col>
                                                <Col md={6}>
                                                    <b>Име на представител: </b>{location.representativeName} <br/>
                                                    <b>Електронна поща на представител: </b>{location.representativeEmail} <br/>
                                                    <b>Изпращане на email: </b>{location.sendEmail ? "Да" : "Не"} <br/>
                                                    <b>Статус: </b>{location.active ? "Active" : "Inactive"} <br/>
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
                                                    onClick={() => setShowModal(true)}
                                                    style={{ marginRight: "10px" }}
                                                >
                                                    Редакция
                                                </Button>
                                                <Button
                                                    variant="primary"
                                                    onClick={() => setShowReportModal(true)}
                                                    style={{ marginRight: "10px" }}
                                                >
                                                    Генериране на справка
                                                </Button>
                                                <Button variant="secondary" onClick={() => navigate(-1)}>
                                                    Назад
                                                </Button>
                                                </Col>
                                            </Row>
                                        </Container>

                                    </Card.Body>
                                </Card>

                                <ReportList locationId={location.id} showAdminAndLocation={false} />
                            </Row>
                        </Container>
                        <Footer />
                    </Col>
                </Row>
            </Container>

            <LocationForm
                show={showModal}
                onHide={() => setShowModal(false)}
                onSubmit={handleEdit}
                initialData={location}
                title="Редакция на локация"
            />

            <GenerateReportModal
                show={showReportModal}
                onHide={() => setShowReportModal(false)}
                onSubmit={handleGenerateReport}
            />

            <Toast
                show={showToast}
                message={toastMessage}
                color={toastColor}
                onClose={() => setShowToast(false)}
            />

        </>

    );
}

export default LocationDetailsPage;
