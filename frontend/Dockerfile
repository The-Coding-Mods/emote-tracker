FROM node:18-alpine
WORKDIR /app

COPY /app/build ./build
COPY /app/node_modules ./node_modules
COPY package.json .

EXPOSE 3000
CMD ["node", "build"]
