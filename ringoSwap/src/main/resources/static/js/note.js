$(document).ready(function() {
    // 페이지 로딩 시 폴더 목록 로드
    loadDirectories();

    // 폴더 클릭 이벤트 리스너
    $(document).on('click', '.directory', function() {
        let dirNum = $(this).data('dir-num');
        loadFilesInDirectory(dirNum);
    });
});

function loadDirectories() {
    $.ajax({
        url: 'dirPrint',
        type: 'POST',
        dataType: 'json',
        success: function(directories) {
            let content = '';
            directories.forEach(function(dir) {
                content += `<div class="directory" data-dir-num="${dir.dir_num}">${dir.dir_name}</div>`;
            });
            $('.dirPrint').html(content);
        },
        error: function(err) {
            console.error('Failed to load directories', err);
        }
    });
}

function loadFilesInDirectory(dirNum) {
    $.ajax({
        url: 'dirOpenFile',
        type: 'POST',
        data: { dir_num: dirNum },
        dataType: 'json',
        success: function(files) {
            let content = '<h3>Files in Directory:</h3>';
            files.forEach(function(file) {
                if (file.fileType === 'note') {
                    content += `<div class="file note">${file.title} (Note)</div>`;
                } else if (file.fileType === 'word') {
                    content += `<div class="file word">${file.title} (Word)</div>`;
                }
            });
            $('.dirPrint').append(content);
        },
        error: function(err) {
            console.error('Failed to load files in directory', err);
        }
    });
}
