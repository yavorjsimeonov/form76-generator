import React, { useState, useEffect } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
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

        <div>
            <div>
                <Header />
                <Menu activeKey="administrations" />
                <Container>
                    <Row>
                        <Col>
                            <h2>Location Details</h2>
                            <Table striped bordered hover>
                                <tbody>
                                <tr>
                                    <td>ID</td>
                                    <td>{location.id}</td>
                                </tr>
                                <tr>
                                    <td>Name</td>
                                    <td>{location.name}</td>
                                </tr>
                                <tr>
                                    <td>Status</td>
                                    <td>{location.active ? "Active" : "Inactive"}</td>
                                </tr>
                                <tr>
                                    <td>Community ID</td>
                                    <td>{location.extCommunityId}</td>
                                </tr>
                                <tr>
                                    <td>Community UUID</td>
                                    <td>{location.extCommunityUuid}</td>
                                </tr>
                                <tr>
                                    <td>Report Algorithm</td>
                                    <td>{location.reportAlgorithm}</td>
                                </tr>
                                <tr>
                                    <td>Representative Name</td>
                                    <td>{location.representativeName}</td>
                                </tr>
                                <tr>
                                    <td>Representative Email</td>
                                    <td>{location.representativeEmail}</td>
                                </tr>
                                </tbody>
                            </Table>
                            <Button
                                variant="warning"
                                onClick={() => setShowModal(true)}
                                style={{ marginRight: "10px" }}
                            >
                                Edit
                            </Button>
                            <Button
                                variant="primary"
                                onClick={() => setShowReportModal(true)}
                                style={{ marginRight: "10px" }}
                            >
                                Generate Report
                            </Button>
                            <Button variant="secondary" onClick={() => navigate(-1)}>
                                Back
                            </Button>

                            <ReportList locationId={location.id} showAdminAndLocation={false} />
                        </Col>
                    </Row>

                    <LocationForm
                        show={showModal}
                        onHide={() => setShowModal(false)}
                        onSubmit={handleEdit}
                        initialData={location}
                        title="Edit Location"
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

                </Container>
                <Footer />
            </div>

        </div>

    );
}

export default LocationDetailsPage;
