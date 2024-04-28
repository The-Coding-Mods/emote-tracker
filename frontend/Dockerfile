FROM node:18-alpine AS BUILDER

RUN npm install -g pnpm

WORKDIR /app
COPY . .
RUN pnpm install --frozen-lockfile
RUN pnpm run build


FROM node:18-alpine
WORKDIR /app
RUN npm install -g pnpm

COPY --from=BUILDER /app/build ./build
COPY package.json .
RUN pnpm install --frozen-lockfile --prod

EXPOSE 3000
CMD ["node", "build"]
