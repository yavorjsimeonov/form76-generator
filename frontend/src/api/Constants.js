const prod = {
    BASE_URL: 'https://form76-generator-mylink.com',
    API_BASE_URL: 'https://form76-generator-mylink.com/api',
}

const dev = {
    BASE_URL: 'http://localhost:8080',
    API_BASE_URL: 'http://localhost:8080/api'
}

export const config = dev /*process.env.NODE_ENV === 'development' ? dev : prod*/ //TODO: how to switch between prod and dev