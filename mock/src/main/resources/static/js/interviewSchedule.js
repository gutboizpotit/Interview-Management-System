function search() {
    var searchQuery = $('#search').val().toLowerCase();
    var interviewerId = $('#filter-interviewer').val();
    var status = $('#filter-status').val().toLowerCase();

    $('#schedule-table-body tr').filter(function() {
        var title = $(this).find('td[data-title]').data('title').toLowerCase();
        var candidateName = $(this).find('td[data-candidate-name]').data('candidate-name').toLowerCase();
        var interviewers = $(this).find('td[data-interviewers]').data('interviewers');
        var scheduleStart = $(this).find('td[data-schedule-start]').data('schedule-start');
        var scheduleEnd = $(this).find('td[data-schedule-end]').data('schedule-end');
        var result = $(this).find('td[data-result]').data('result').toLowerCase();
        var statusValue = $(this).find('td[data-status]').data('status').toLowerCase();
        var jobTitle = $(this).find('td[data-job-title]').data('job-title').toLowerCase();

        var interviewerMatch = true;
        if (interviewerId) {
            interviewerMatch = interviewers.includes(interviewerId);
        }

        var statusMatch = true;
        if (status) {
            statusMatch = statusValue.includes(status);
        }

        return (title.includes(searchQuery) || candidateName.includes(searchQuery) ||
                jobTitle.includes(searchQuery) || result.includes(searchQuery) ||
                scheduleStart.includes(searchQuery) || scheduleEnd.includes(searchQuery))
                && interviewerMatch && statusMatch;
    }).show();

    $('#schedule-table-body tr').filter(function() {
        var title = $(this).find('td[data-title]').data('title').toLowerCase();
        var candidateName = $(this).find('td[data-candidate-name]').data('candidate-name').toLowerCase();
        var interviewers = $(this).find('td[data-interviewers]').data('interviewers');
        var scheduleStart = $(this).find('td[data-schedule-start]').data('schedule-start');
        var scheduleEnd = $(this).find('td[data-schedule-end]').data('schedule-end');
        var result = $(this).find('td[data-result]').data('result').toLowerCase();
        var statusValue = $(this).find('td[data-status]').data('status').toLowerCase();
        var jobTitle = $(this).find('td[data-job-title]').data('job-title').toLowerCase();

        var interviewerMatch = true;
        if (interviewerId) {
            interviewerMatch = interviewers.includes(interviewerId);
        }

        var statusMatch = true;
        if (status) {
            statusMatch = statusValue.includes(status);
        }

        return !((title.includes(searchQuery) || jobTitle.includes(searchQuery) ||
                result.includes(searchQuery) || candidateName.includes(searchQuery)||
                scheduleStart.includes(searchQuery) || scheduleEnd.includes(searchQuery))  &&
                interviewerMatch && statusMatch);
    }).hide();

}

document.addEventListener('DOMContentLoaded', function() {
    var toast = document.getElementById('toast');

    if (toast) {
        var message = document.getElementById('toast-content').textContent;
        console.log(message)

        if (message.includes("successfully!")) {
            toast.className = 'toast show success';
        } else if (message.includes("fail")) {
            toast.className = 'toast show error';
        } else {
            toast.className = 'toast show';
        }

        toast.classList.add('show');
        setTimeout(function() {
            toast.classList.remove('show');
        }, 3000);
    }
});