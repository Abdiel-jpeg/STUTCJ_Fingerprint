FROM python:3.13.6-alpine3.22

WORKDIR /app

COPY . /app

RUN pip install blinker charset-normalizer click flask itsdangerous jinja2 markupsafe mysql-connector-python pillow python-dotenv reportlab werkzeug

CMD ["python", "-m", "flask", "run"]

