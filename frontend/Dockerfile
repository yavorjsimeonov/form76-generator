# build step
FROM node:16.13.2-alpine as build
WORKDIR /app
COPY package.json ./
RUN npm install
RUN npm install --save-dev web-vitals
RUN npm install --save-dev react-router-dom
RUN npm install --save-dev react-bootstrap bootstrap

COPY . ./
RUN npm run build

# release step
FROM nginx:1.21.5-alpine as release
COPY --from=build /app/build /usr/share/nginx/html/
RUN rm -rf /etc/nginx/conf.d
COPY conf /etc/nginx
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]