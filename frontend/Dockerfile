FROM node:22-alpine AS BUILDER

RUN npm install -g pnpm@10.6.5

WORKDIR /app
COPY . .
RUN pnpm install --frozen-lockfile
RUN pnpm run build


FROM node:22-alpine
WORKDIR /app
RUN npm install -g pnpm@10.6.5

COPY --from=BUILDER /app/build ./build
COPY package.json .
COPY pnpm-lock.yaml .
RUN pnpm install --frozen-lockfile --prod

EXPOSE 3000
CMD ["node", "build"]
