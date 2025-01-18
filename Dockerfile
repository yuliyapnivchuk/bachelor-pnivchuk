FROM postgres:14.3

# Set environment variables for PostgreSQL
ENV POSTGRES_DB=itstep
ENV POSTGRES_USER=itstep
ENV POSTGRES_PASSWORD=itstep123
# Postgres default port
EXPOSE 5432