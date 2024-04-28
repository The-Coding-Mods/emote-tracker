FROM node:18-alpine AS BUILDER

RUN npm install -g pnpm

WORKDIR /app
COPY . .
RUN pnpm install --frozen-lockfile
RUN pnpm run build --prod


FROM node:18-alpine
WORKDIR /app

COPY --from=BUILDER /app/build ./build
COPY --from=BUILDER /app/node_modules ./node_modules
COPY package.json .

EXPOSE 3000
CMD ["node", "build"]
