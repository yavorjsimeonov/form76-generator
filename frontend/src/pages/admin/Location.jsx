import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Row, Col } from "react-bootstrap";
import Header from "../../components/common/Header";
import Footer from "../../components/common/Footer";
import LeftMenu from "../../components/common/LeftMenu";
import LocationForm from "../../components/LocationForm";
import GenerateReportModal from "../../components/GenerateReportForm";
import Toast from "../../components/common/Toast";
import ReportList from "../../components/ReportList";
import LocationDetailsCard from "../../components/LocationDetailsCard";
import { useLocationDetails } from "../../hooks/useLocationDetails";
import { useToast } from "../../hooks/useToast";
import { form76GeneratorApi } from "../../api/Form76GeneratorApi";
import { useAuth } from "../../components/common/AuthContext";

function LocationDetailsPage() {
        const { id } = useParams();
        const navigate = useNavigate();
        const Auth = useAuth();
        const user = Auth.getUser();


    const { location, loading, error, setLocation } = useLocationDetails(id, user);
    const { showToast, toastMessage, toastColor, triggerToast, closeToast } = useToast();

    const [showModal, setShowModal] = useState(false);
    const [showReportModal, setShowReportModal] = useState(false);

    const handleEdit = async (updatedLocation) => {
        try {
            const response = await form76GeneratorApi.updateLocation(user, id, updatedLocation);
            setLocation(response.data);
            triggerToast("Локацията беше успешно редактирана.", "success");
        } catch (error) {
            triggerToast("Редакцията на локацията беше неуспешна.", "danger");
        } finally {
            setShowModal(false);
        }
    };

    const handleGenerateReport = async (reportRequest) => {
        try {
            const request = { locationId: location.id, ...reportRequest };
            const response = await form76GeneratorApi.generateReportForLocation(user, location.id, request);
            if (response.status === 200 && response.data) {
                triggerToast(`Справка задействана! Имейл ще бъде изпратен до ${location.representativeEmail}`, "success");
            }
        } catch (error) {
            triggerToast(`Грешка при генериране на справка за локация ${location.name}`, "danger");
        }
    };

    if (loading) return <p>Зареждане...</p>;
    if (error) return <p>Грешка: {error}</p>;
    if (!location) return <p>Локацията не беше намерена.</p>;

    return (
        <>
            <Container fluid>
                <Row>
                    <Col md={2}><LeftMenu activeKey="administrations" /></Col>
                    <Col md={10}>
                        <Header />
                        <Container fluid="md">
                            <Row className="justify-content-md-center">
                                <h2>Локация: <span className="object">{location.name}</span></h2>
                                <LocationDetailsCard
                                    location={location}
                                    onEdit={() => setShowModal(true)}
                                    onGenerate={() => setShowReportModal(true)}
                                    onBack={() => navigate(-1)}
                                />
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
                onClose={closeToast}
            />
        </>
    );
}

export default LocationDetailsPage;
