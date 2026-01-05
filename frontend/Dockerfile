# 1. Use Node.js (Alpine Linux) as the base
FROM node:18-alpine

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy package files first (for better caching)
COPY package*.json ./

# 4. Install dependencies
RUN npm install

# 5. Copy the rest of your code
COPY . .

# 6. Build the Next.js app for production
RUN npm run build

# 7. Expose the port Next.js runs on
EXPOSE 3000

# 8. Start the app
CMD ["npm", "start"]