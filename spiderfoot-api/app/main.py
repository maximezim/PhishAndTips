import uvicorn
from fastapi import FastAPI
from app.router import router
from app.models import Base
from app.database import engine

Base.metadata.create_all(bind=engine)

app = FastAPI()

app.include_router(router, prefix="/internal/spiderfoot", tags=["spiderfoot"])

if __name__ == "__main__":
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000, reload=True)
