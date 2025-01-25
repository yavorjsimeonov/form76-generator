import React, { useState } from "react";
import { Table, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { form76GeneratorApi } from "../api/Form76GeneratorApi";
import LocationForm from "./LocationForm";
import { useAuth } from "./common/AuthContext";

function LocationList({ locations, administrationId, onLocationCreated }) {
    const Auth = useAuth();
    const user = Auth.getUser();
    const navigate = useNavigate();
    const [showLocationModal, setShowLocationModal] = useState(false);

    const handleCreateLocation = async (newLocation) => {
        try {
            const response = await form76GeneratorApi.createLocation(user, {
                ...newLocation,
                administrationId,
            });
            onLocationCreated(response.data); // Update parent state with the new location
        } catch (error) {
            console.error("Error creating location:", error);
        } finally {
            setShowLocationModal(false);
        }
    };

    return (
        <div>
            <h3>Locations</h3>

            {/* Create Location Button */}
            <Button
                variant="primary"
                onClick={() => setShowLocationModal(true)}
                style={{ marginBottom: "20px" }}
            >
                Create Location
            </Button>

            {locations.length > 0 ? (
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Status</th>
                        <th>Community ID</th>
                        <th>Community UUID</th>
                        <th>Report Algorithm</th>
                        <th>Representative Name</th>
                        <th>Representative Email</th>
                    </tr>
                    </thead>
                    <tbody>
                    {locations.map((location) => (
                        <tr key={location.id}>
                            <td>
                                {/* Link to Location Details Page */}
                                <Button
                                    variant="link"
                                    onClick={() =>
                                        navigate(`/locations/${location.id}`, {
                                            state: { location },
                                        })
                                    }
                                >
                                    {location.name || "N/A"}
                                </Button>
                            </td>
                            <td>{location.active ? "Active" : "Inactive"}</td>
                            <td>{location.extCommunityId || "N/A"}</td>
                            <td>{location.extCommunityUuid || "N/A"}</td>
                            <td>{location.reportAlgorithm || "N/A"}</td>
                            <td>{location.representativeName || "N/A"}</td>
                            <td>{location.representativeEmail || "N/A"}</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            ) : (
                <p>No locations found for this administration.</p>
            )}

            {/* Modal for Creating Location */}
            <LocationForm
                show={showLocationModal}
                onHide={() => setShowLocationModal(false)}
                onSubmit={handleCreateLocation}
                initialData={{
                    name: "",
                    extCommunityId: "",
                    extCommunityUuid: "",
                    representativeName: "",
                    representativeEmail: "",
                    reportAlgorithm: "EVERY_IN_OUT",
                    active: true,
                }}
                title="Create Location"
            />
        </div>
    );
}

export default LocationList;
