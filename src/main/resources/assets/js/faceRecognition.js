const detectionOptions = {
    withLandmarks: true,
    withDescriptors: false,
};

const faceapi = ml5.faceApi(detectionOptions, analyzeImage);

function analyzeImage() {
    const imgEl = document.body.lastElementChild.lastElementChild.lastElementChild
    const textEl = document.body.lastElementChild.lastElementChild.firstElementChild

    faceapi.detect(imgEl, (err, results) => {
        const numFaces = results.length
        textEl.textContent = `${numFaces} faces detected`
    });
}