const gulp = require("gulp");
const { src, dest, watch, series } = require("gulp");

const sass = require("gulp-sass")(require("sass"));

const browserSync = require("browser-sync").create();

// Sass Task
function scssTask() {
  return src("./bootstrap/scss/*.scss")
    .pipe(sass())
    .pipe(gulp.dest("./bootstrap/css/"));
}

// BrowserSync Task
function browserSyncServer(cb) {
  browserSync.init({
    server: {
      baseDir: ".",
    },
  });
  cb();
}

function browserSyncReload(cb) {
  browserSync.reload();
  cb();
}

// Watch Task
function watchTask() {
  watch(["*.html", "./bootstrap/js/**/*.js"], browserSyncReload);
  watch(["./bootstrap/scss/**/*.scss"], series(scssTask, browserSyncReload));
}

exports.default = series(scssTask, browserSyncServer, watchTask);
