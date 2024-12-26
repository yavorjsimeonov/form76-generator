const prod = {
    url: {
        API_BASE_URL: 'https://form76-generator-mylink.com',
    }
}

const dev = {
    url: {
        API_BASE_URL: 'http://localhost:8080'
    }
}

export const config = dev /*process.env.NODE_ENV === 'development' ? dev : prod*/ //TODO: how to switch between prod and dev