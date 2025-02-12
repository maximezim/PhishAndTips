FROM node:18-alpine AS builder

ARG VITE_GATEWAY_URL
ARG VITE_API_KEY
ARG VITE_S3_ENDPOINT

ENV VITE_GATEWAY_URL=$VITE_GATEWAY_URL
ENV VITE_API_KEY=$VITE_API_KEY
ENV VITE_S3_ENDPOINT=$VITE_S3_ENDPOINT

WORKDIR /app
COPY package*.json .
RUN npm install
COPY . .

RUN npm run build
RUN npm prune --production

FROM node:18-alpine
LABEL maintainer1="Tristan Versel"
LABEL maintainer2="Bastien Ubassy"
WORKDIR /app
COPY --from=builder /app/build build/
COPY --from=builder /app/node_modules node_modules/
COPY package.json .
EXPOSE 3000
ENV NODE_ENV=production
CMD [ "node", "build" ]