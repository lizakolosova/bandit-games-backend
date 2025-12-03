*** to run docker

first of all:

# Login to GitLab Container Registry
docker login registry.gitlab.com

# When prompted:
# Username: your-gitlab-username
# Password: use a GitLab Personal Access Token (NOT your password)


** image:
./gradlew bootBuildImage --imageName=registry.gitlab.com/kdg-ti/inf-curriculum/domain-integration/integration-5/2025-2026/team8/backend:latest

docker push registry.gitlab.com/kdg-ti/inf-curriculum/domain-integration/integration-5/2025-2026/team8/backend:latest

cd infrastructure
docker-compose up -d