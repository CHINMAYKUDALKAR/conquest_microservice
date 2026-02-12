/**
 * Conquest Frontend Logic
 * Vanilla JS — Connects to API Gateway on port 9090
 */

const API_BASE = 'http://localhost:9090';

const app = {
    state: {
        view: 'dashboard',
        tickets: [],
        users: []
    },

    init: async () => {
        app.renderDashboard();
    },

    fetchData: async () => {
        try {
            const ticketRes = await fetch(`${API_BASE}/ticket-service/tickets`);
            if (ticketRes.ok) {
                app.state.tickets = await ticketRes.json();
            } else {
                app.state.tickets = [];
            }
        } catch (e) {
            console.error('Failed to fetch tickets:', e);
            app.state.tickets = [];
        }

        const total = app.state.tickets.length;
        const closed = app.state.tickets.filter(t => t.status === 'CLOSED').length;
        const assigned = app.state.tickets.filter(t => t.status === 'ASSIGNED').length;
        const open = total - closed;

        const totalEl = document.getElementById('stat-total-tickets');
        const openEl = document.getElementById('stat-open-tickets');
        const closedEl = document.getElementById('stat-closed-tickets');
        if (totalEl) totalEl.textContent = total;
        if (openEl) openEl.textContent = open;
        if (closedEl) closedEl.textContent = closed;
    },

    navigate: (viewName) => {
        app.state.view = viewName;

        document.querySelectorAll('nav a').forEach(el => el.classList.remove('active'));
        const navEl = document.getElementById(`nav-${viewName}`);
        if (navEl) navEl.classList.add('active');

        const main = document.getElementById('main-content');

        if (viewName === 'dashboard') {
            app.renderDashboardHTML(main);
            app.fetchData().then(() => app.renderRecentTickets());
        } else if (viewName === 'tickets') {
            app.renderTicketList(main);
        } else if (viewName === 'users') {
            app.renderUserList(main);
        } else {
            main.innerHTML = `
                <header style="margin-bottom: var(--space-lg);">
                    <h1>${viewName.charAt(0).toUpperCase() + viewName.slice(1)}</h1>
                    <p>This section is under development.</p>
                </header>
            `;
        }
    },

    renderDashboardHTML: (container) => {
        container.innerHTML = `
            <header class="flex-between" style="margin-bottom: var(--space-lg);">
                <div>
                    <h1>Overview</h1>
                    <p>Welcome back. Here's what's happening today.</p>
                </div>
                <button class="btn btn-primary" onclick="app.openCreateTicketModal()">+ New Ticket</button>
            </header>
            <div class="grid-3" style="margin-bottom: var(--space-lg);">
                <div class="card">
                    <span class="label">Total Tickets</span>
                    <h2 id="stat-total-tickets">–</h2>
                </div>
                <div class="card">
                    <span class="label">Open / Assigned</span>
                    <h2 id="stat-open-tickets" style="color: var(--color-warning);">–</h2>
                </div>
                <div class="card">
                    <span class="label">Closed</span>
                    <h2 id="stat-closed-tickets" style="color: var(--color-success);">–</h2>
                </div>
            </div>
            <section>
                <div class="flex-between">
                    <h3>Recent Tickets</h3>
                    <a href="#" onclick="app.navigate('tickets')" style="font-size: var(--text-sm);">View all &rarr;</a>
                </div>
                <div id="recent-tickets-list" style="margin-top: var(--space-sm);">
                    <div class="card" style="text-align:center; padding: var(--space-xl); color: var(--color-ink-subtle);">
                        Loading...
                    </div>
                </div>
            </section>
        `;
    },

    renderDashboard: () => {
        const main = document.getElementById('main-content');
        app.renderDashboardHTML(main);
        app.fetchData().then(() => app.renderRecentTickets());
    },

    renderRecentTickets: () => {
        const listContainer = document.getElementById('recent-tickets-list');
        if (!listContainer) return;

        listContainer.innerHTML = '';

        const recent = app.state.tickets.slice(0, 5);

        if (recent.length === 0) {
            listContainer.innerHTML = `
                <div class="card" style="text-align:center; padding: var(--space-lg); color: var(--color-ink-subtle);">
                    No tickets yet. Create one to get started.
                </div>`;
            return;
        }

        recent.forEach(ticket => {
            const statusClass = ticket.status === 'CLOSED' ? 'badge-closed' : (ticket.status === 'ASSIGNED' ? 'badge-urgent' : 'badge-open');
            const card = document.createElement('div');
            card.className = 'card flex-between';
            card.style.cursor = 'pointer';
            card.innerHTML = `
                <div>
                    <h4 style="margin-bottom:4px;">${ticket.issue || 'No issue description'}</h4>
                    <p style="font-size: var(--text-xs); margin:0; color: var(--color-ink-subtle);">
                        Ticket #${ticket.id} &middot; Raised by User #${ticket.raisedBy || '–'} 
                        ${ticket.raisedOn ? '&middot; ' + ticket.raisedOn : ''}
                    </p>
                </div>
                <span class="badge ${statusClass}">${ticket.status || 'OPEN'}</span>
            `;
            listContainer.appendChild(card);
        });
    },

    renderTicketList: (container) => {
        let html = `
            <header class="flex-between" style="margin-bottom: var(--space-lg);">
                <h1>All Tickets</h1>
                <button class="btn btn-primary" onclick="app.openCreateTicketModal()">+ New Ticket</button>
            </header>
        `;

        if (app.state.tickets.length === 0) {
            html += `<div class="card" style="text-align:center; padding: var(--space-lg); color: var(--color-ink-subtle);">
                No tickets found.
            </div>`;
        } else {
            app.state.tickets.forEach(ticket => {
                const statusClass = ticket.status === 'CLOSED' ? 'badge-closed' : (ticket.status === 'ASSIGNED' ? 'badge-urgent' : 'badge-open');
                html += `
                    <div class="card">
                        <div class="flex-between" style="margin-bottom: var(--space-xs);">
                            <h3>${ticket.issue || 'No issue description'}</h3>
                            <span class="badge ${statusClass}">${ticket.status || 'OPEN'}</span>
                        </div>
                        <div style="font-size: var(--text-sm); color: var(--color-ink-subtle);">
                            <span>Ticket #${ticket.id}</span>
                            <span style="margin: 0 8px;">&middot;</span>
                            <span>Raised by User #${ticket.raisedBy || '–'}</span>
                            ${ticket.raisedOn ? `<span style="margin: 0 8px;">&middot;</span><span>${ticket.raisedOn}</span>` : ''}
                            ${ticket.assignedTo ? `<span style="margin: 0 8px;">&middot;</span><span>Assigned to User #${ticket.assignedTo}</span>` : ''}
                        </div>
                    </div>
                `;
            });
        }

        container.innerHTML = html;
    },

    renderUserList: async (container) => {
        container.innerHTML = `
            <header style="margin-bottom: var(--space-lg);">
                <h1>User Directory</h1>
                <p>All registered users across the system.</p>
            </header>
            <div class="card" style="text-align:center; padding: var(--space-lg); color: var(--color-ink-subtle);">Loading users...</div>
        `;

        try {
            const res = await fetch(`${API_BASE}/user-micro-service/users`);
            const users = await res.json();

            let html = `
                <header style="margin-bottom: var(--space-lg);">
                    <h1>User Directory</h1>
                    <p>${users.length} users registered.</p>
                </header>
                <div class="card" style="padding:0; overflow:hidden;">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Role</th>
                            </tr>
                        </thead>
                        <tbody>
            `;

            users.forEach(user => {
                const roleBadge = user.role === 'CUSTOMER' ? 'badge-open' : 'badge-closed';
                html += `
                    <tr>
                        <td>#${user.id}</td>
                        <td style="font-weight:500;">${user.name}</td>
                        <td>${user.email}</td>
                        <td>${user.phone || '–'}</td>
                        <td><span class="badge ${roleBadge}">${user.role}</span></td>
                    </tr>
                `;
            });

            html += '</tbody></table></div>';
            container.innerHTML = html;
        } catch (e) {
            container.innerHTML = `
                <header style="margin-bottom: var(--space-lg);">
                    <h1>User Directory</h1>
                </header>
                <div class="card" style="padding: var(--space-lg); color: var(--color-danger);">
                    Could not load users. Make sure User Service is running on port 9001.
                </div>
            `;
        }
    },

    openCreateTicketModal: () => {
        const modal = document.getElementById('modal-overlay');
        modal.classList.remove('hidden');
        requestAnimationFrame(() => {
            modal.style.opacity = '1';
            modal.querySelector('.modal-body').style.transform = 'translateY(0)';
        });
    },

    closeModal: () => {
        const modal = document.getElementById('modal-overlay');
        modal.style.opacity = '0';
        modal.querySelector('.modal-body').style.transform = 'translateY(10px)';
        setTimeout(() => modal.classList.add('hidden'), 200);
    },

    submitTicket: async (e) => {
        e.preventDefault();
        const issue = document.getElementById('ticket-issue').value;
        const raisedBy = document.getElementById('ticket-raisedby').value;

        const submitBtn = e.target.querySelector('button[type="submit"]');
        submitBtn.textContent = 'Creating...';
        submitBtn.disabled = true;

        try {
            const res = await fetch(`${API_BASE}/ticket-service/tickets`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    issue: issue,
                    raisedBy: parseInt(raisedBy),
                    status: 'OPEN'
                })
            });

            if (res.ok) {
                app.closeModal();
                e.target.reset();
                // Refresh dashboard
                await app.fetchData();
                if (app.state.view === 'dashboard') {
                    app.renderRecentTickets();
                } else if (app.state.view === 'tickets') {
                    app.renderTicketList(document.getElementById('main-content'));
                }
            } else {
                alert('Failed to create ticket. Server returned ' + res.status);
            }
        } catch (err) {
            console.error(err);
            alert('Network error creating ticket.');
        } finally {
            submitBtn.textContent = 'Create Ticket';
            submitBtn.disabled = false;
        }
    }
};

document.addEventListener('DOMContentLoaded', app.init);
