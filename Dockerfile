FROM ubuntu
WORKDIR /
WORKDIR /app

RUN apt -y update
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=US/Eastern
RUN apt-get install -y tzdata
RUN apt-get -y install python3-pip git cmake libxml2-dev wordnet-dev openjdk-11-jdk
# RUN git clone --recursive https://github.com/SCANL/ensemble_tagger.git
COPY ensemble_tagger /app/ensemble_tagger

RUN cd ensemble_tagger && pip3 install -r requirements.txt
ENV PYTHONPATH=/app/ensemble_tagger
ENV PERL5LIB=/app/ensemble_tagger/POSSE/Scripts

RUN pip3 install git+https://github.com/cnewman/spiral.git

WORKDIR /app/ensemble_tagger/ensemble_tagger_implementation

CMD ["python3", "routes.py"]
