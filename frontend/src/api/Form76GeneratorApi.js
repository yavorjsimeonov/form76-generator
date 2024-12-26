import axios from 'axios'
import { config } from './Constants'

export const form76GeneratorApi = {
    authenticate
    //getAdministrations,
    //viewAdministration,etc....
}

function authenticate(username, password) {
    return instance.post('/auth/authenticate', { username, password }, {
        headers: { 'Content-type': 'application/json' }
    })
}

// -- Axios

const instance = axios.create({
    baseURL: config.url.API_BASE_URL
})

// -- Helper functions

function basicAuth(user) {
    return `Basic ${user.authdata}`
}