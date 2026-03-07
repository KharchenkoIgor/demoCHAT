export async function request(url,
                       method = 'GET',
                       body = null,
                       errorMessage = 'API Error') {
    const options = {
        method,
        headers: body ? { 'Content-Type': 'application/json' } : {}
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);

    if (!response.ok) {
        throw new Error(errorMessage);
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}