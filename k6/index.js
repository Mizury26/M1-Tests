import http from 'k6/http';
import { Rate } from 'k6/metrics';
import { sleep } from 'k6';

const failureRate = new Rate('failed_requests');

const books = [
    { title: 'Clean Code', author: 'Robert Martin' },
    { title: 'DDD', author: 'Eric Evans' },
    { title: 'The Pragmatic Programmer', author: 'Andrew Hunt' },
    { title: 'Refactoring', author: 'Martin Fowler' },
    { title: 'Design Patterns', author: 'Gang of Four' },
];

export function test_api_endpoints_config() {
    const isPost = Math.random() < 0.5;

    if (isPost) {
        const book = books[Math.floor(Math.random() * books.length)];
        const res = http.post(
            'http://localhost:8080/books',
            JSON.stringify(book),
            { headers: { 'Content-Type': 'application/json' } }
        );
        failureRate.add(res.status !== 201);
    } else {
        const res = http.get('http://localhost:8080/books');
        failureRate.add(res.status !== 200);
    }

    sleep(1);
}
