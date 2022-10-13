function uploadFile(fileInputId) {
    const file = document.getElementById(fileInputId).files[0];

    fetch('/api/blob', {
        method: 'POST',
        body: file
    })
        .then((response) => response.json())
        .then((result) => {
            window.location.href = `http://0.0.0.0:8080/file/${result.UUID}`
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}
