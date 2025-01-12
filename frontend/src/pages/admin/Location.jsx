import React, { useState, useEffect } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import LocationFormModal from "../../components/LocationForm";
import GenerateReportModal from "../../components/GenerateReportForm";

function LocationDetailsPage() {
    const { id } = useParams();
    const { state } = useLocation();
    const navigate = useNavigate();

    const [location, setLocation] = useState(state?.location || null);
    const [loading, setLoading] = useState(!state?.location);
    const [error, setError] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showReportModal, setShowReportModal] = useState(false);
    const [showToast, setShowToast] = useState(false);

    useEffect(() => {
        if (!location) {
            const fetchLocation = async () => {
                try {
                    const response = await fetch(`http://localhost:8080/locations/${id}`);
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    const data = await response.json();
                    setLocation(data);
                } catch (error) {
                    setError(error.message);
                } finally {
                    setLoading(false);
                }
            };

            fetchLocation();
        }
    }, [id, location]);

    const handleEdit = async (updatedLocation) => {
        try {
            const response = await fetch(`http://localhost:8080/locations/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(updatedLocation),
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const editedLocation = await response.json();
            setLocation(editedLocation);
        } catch (error) {
            console.error("Error editing location:", error);
        } finally {
            setShowModal(false);
        }
    };

    const handleGenerateReport = (reportRequest) => {
        const request = {
            locationId: location.id,
            ...reportRequest,
        };

        console.log("Generating Report with Request:", request);

        fetch(`http://localhost:8080/locations/${location.id}/generate`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                console.log("Report Generation triggered successfully:", data);
                setShowToast(true);
            })
            .catch((error) => {
                console.error("Error generating report:", error);
            });
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
                </Col>
            </Row>

            {/* Modal for Editing Location */}
            <LocationFormModal
                show={showModal}
                onHide={() => setShowModal(false)}
                onSubmit={handleEdit}
                initialData={location}
                title="Edit Location"
            />

            {/* Modal for Generating Report */}
            <GenerateReportModal
                show={showReportModal}
                onHide={() => setShowReportModal(false)}
                onSubmit={handleGenerateReport}
            />
            <div>
                {showToast && (
                <div className="toast align-items-center text-bg-primary border-0" role="alert" aria-live="assertive"
                     aria-atomic="true">
                    <div className="d-flex">
                        <div className="toast-body">
                            Report generation triggered successfully. You will receive an email on {location.representativeEmail}
                        </div>
                        <button type="button" className="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                                aria-label="Close"></button>
                    </div>
                </div>)}
            </div>

        </Container>
    );
}

export default LocationDetailsPage;
