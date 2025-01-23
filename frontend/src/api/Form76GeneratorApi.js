import axios from 'axios'
import { config } from './Constants'

export const form76GeneratorApi = {
    authenticate,
    getAdministrations,
    getAdministration,
    createAdministration,
    updateAdministration,
    getLocation,
    createLocation,
    updateLocation,
    generateReportForLocation,
    getUsers,
    createUser
}

function authenticate(username, password) {
    return instance.post('/auth/authenticate', { username, password }, {
        headers: {
            'Content-type': 'application/json',
            'Access-Control-Allow-Origin': "*"
        }
    })
}

function getAdministrations(user) {
    return instance.get('/api/administrations', {
        headers: {
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}

function getAdministration(user, administrationId) {
    return instance.get(`/api/administrations/${administrationId}`, {
        headers: {
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}

function createAdministration(user, newAdministration) {
    return instance.post(`/api/administrations`, newAdministration, {
        headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}

function updateAdministration(user, administrationId, updatedAdministration) {
    return instance.put(`/api/administrations/${administrationId}`, updatedAdministration, {
        headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}

function getLocation(user, locationId) {
    return instance.get(`/api/locations/${locationId}`, {
        headers: {
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}

function createLocation(user, newLocation) {
    return instance.post(`/api/locations`, newLocation, {
        headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}

function updateLocation(user, locationId, updatedLocations) {
    return instance.put(`/api/locations/${locationId}`, updatedLocations, {
        headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}

function getUsers(user) {
    return instance.get('/api/users', {
        headers: {
            "Content-Type": "application/json",
                'Access-Control-Allow-Origin': "*",
                'Authorization': basicAuth(user)
        }
    })
}

function createUser(user, newUser) {
    return instance.post('/api/users', newUser, {
        headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}



function generateReportForLocation(user, locationId, reportGenerationRequest) {
    console.log("Generating Report with Request:", reportGenerationRequest);

    return instance.post(`/api/locations/${locationId}/generate`, reportGenerationRequest, {
        headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': "*",
            'Authorization': basicAuth(user)
        }
    })
}

// -- Axios

const instance = axios.create({
    baseURL: config.BASE_URL
})

// -- Helper functions

function basicAuth(user) {
    return `Basic ${user.authdata}`
}