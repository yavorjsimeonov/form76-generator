import React from "react";
import { Table, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

function LocationList({ locations }) {
    const navigate = useNavigate();

    return (
        <div>
            <h3>Locations</h3>
            {locations.length > 0 ? (
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>ID</th>
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
                            <td>{location.id || "N/A"}</td>
                            <td>
                                <Button
                                    variant="link"
                                    onClick={() =>
                                        navigate(`/locations/${location.id}`, { state: { location } })
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
        </div>
    );
}

export default LocationList;
