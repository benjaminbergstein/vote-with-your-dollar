DOCKER_IMAGE_NAME=benbergstein/places-annotations
GIT_SHA=$(shell git show-ref --head --hash=7 | head -1)
SSH_KEY=${HOME}/.ssh/vote-with-your-dollar.pem
SSH_HOST=places.benjaminbergstein.com

build:
	docker build . -t ${DOCKER_IMAGE_NAME}:latest

tag:
	docker tag ${DOCKER_IMAGE_NAME}:latest ${DOCKER_IMAGE_NAME}:${GIT_SHA}

push:
	docker push ${DOCKER_IMAGE_NAME}:latest
	docker push ${DOCKER_IMAGE_NAME}:${GIT_SHA}

release: build tag push
	ssh -i ${SSH_KEY} ec2-user@${SSH_HOST} "sudo ./dev/restart.sh"

ssh:
	ssh -i ${SSH_KEY} ec2-user@${SSH_HOST}
