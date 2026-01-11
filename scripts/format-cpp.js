const {execSync} = require('child_process');
const {glob} = require('glob');
const fs = require('fs');
const path = require('path');
const os = require('os');

// 进度条类
class ProgressBar {
    constructor(total, barLength = 40) {
        this.total = total;
        this.current = 0;
        this.barLength = barLength;
        this.startTime = Date.now();
    }

    update(current) {
        this.current = current;
        const progress = this.current / this.total;
        const filledLength = Math.floor(progress * this.barLength);
        const bar = '█'.repeat(filledLength) + '░'.repeat(this.barLength - filledLength);
        const percentage = (progress * 100).toFixed(2);

        // 计算剩余时间
        const elapsedTime = Date.now() - this.startTime;
        const estimatedTotalTime = progress > 0 ? elapsedTime / progress : 0;
        const remainingTime = estimatedTotalTime - elapsedTime;

        // 格式化时间
        const formatTime = (ms) => {
            if (ms < 1000) return `${ms}ms`;
            const seconds = Math.floor(ms / 1000);
            if (seconds < 60) return `${seconds}s`;
            const minutes = Math.floor(seconds / 60);
            const remainingSeconds = seconds % 60;
            return `${minutes}m ${remainingSeconds}s`;
        };

        process.stdout.clearLine();
        process.stdout.cursorTo(0);

        if (this.current < this.total) {
            process.stdout.write(
                `\x1b[36m[${bar}]\x1b[0m \x1b[33m${percentage}%\x1b[0m ` +
                `(\x1b[32m${this.current}/${this.total}\x1b[0m) ` +
                `⏱️ ${formatTime(elapsedTime)} | ⏳ ${formatTime(remainingTime)}`
            );
        } else {
            process.stdout.write(
                `\x1b[36m[${bar}]\x1b[0m \x1b[32m${percentage}%\x1b[0m ` +
                `(\x1b[32m${this.current}/${this.total}\x1b[0m) ` +
                `✅ 完成 ⏱️ ${formatTime(elapsedTime)}`
            );
            process.stdout.write('\n');
        }
    }

    finish() {
        this.update(this.total);
    }
}

async function formatCppFiles() {
    console.log('\x1b[36m🚀 开始格式化 Java 文件...\x1b[0m');
    console.log('\x1b[90m📁 工作目录:', process.cwd(), '\x1b[0m');

    try {
        // 显示正在搜索文件
        console.log('\x1b[33m🔍 正在搜索文件...\x1b[0m');

        // 使用 glob 获取所有 Java 文件
        const files = await glob('**/*.java', {
            ignore: [
                'node_modules/**',
                'gradle/**',
                '.gradle/**',
                '.git/**',
                '**/third_party/**',
                '**/vendor/**',
                'source/data/library/**',
                '.idea/**',
                '.github/**',
                'scripts/**',
                'run/**'
            ]
        });

        const total = files.length;
        console.log(`\x1b[32m📊 找到 ${total} 个 Java 文件\x1b[0m`);

        if (total === 0) {
            console.log('\x1b[33mℹ️  未找到 Java 文件\x1b[0m');
            return;
        }

        // 显示前10个文件预览
        console.log('\x1b[90m📄 文件预览:\x1b[0m');
        files.slice(0, 10).forEach((file, i) => {
            console.log(`  \x1b[90m${i + 1}. ${file}\x1b[0m`);
        });
        if (total > 10) {
            console.log(`  \x1b[90m... 还有 ${total - 10} 个文件\x1b[0m`);
        }
        console.log();

        // 创建进度条
        const progressBar = new ProgressBar(total, 30);

        // 收集成功和失败的文件
        const successFiles = [];
        const failedFiles = [];

        // 逐个文件格式化
        for (let i = 0; i < files.length; i++) {
            const file = files[i];

            try {
                if (os.platform() === 'win32') {
                    // Windows 需要特殊处理路径
                    execSync(`clang-format -i --style=file "${file}"`, {
                        stdio: 'pipe',
                        shell: true
                    });
                } else {
                    execSync(`clang-format -i --style=file "${file}"`, {
                        stdio: 'pipe'
                    });
                }
                successFiles.push(file);
            } catch (error) {
                failedFiles.push({file, error: error.message});
            }

            // 更新进度条
            progressBar.update(i + 1);

            // 每处理10个文件暂停一下，避免太快看不到进度
            if (i % 10 === 0 && i > 0) {
                await new Promise(resolve => setTimeout(resolve, 10));
            }
        }

        console.log('\n\x1b[36m📋 格式化结果:\x1b[0m');
        console.log(`  \x1b[32m✅ 成功: ${successFiles.length} 个文件\x1b[0m`);

        if (failedFiles.length > 0) {
            console.log(`  \x1b[31m❌ 失败: ${failedFiles.length} 个文件\x1b[0m`);
            console.log('\n\x1b[33m📝 失败文件列表:\x1b[0m');
            failedFiles.forEach(({file, error}, index) => {
                console.log(`  ${index + 1}. \x1b[31m${file}\x1b[0m`);
                console.log(`     \x1b[90m错误: ${error}\x1b[0m`);
            });

            // 将失败文件保存到日志
            const logDir = path.join(process.cwd(), 'logs');
            if (!fs.existsSync(logDir)) {
                fs.mkdirSync(logDir, {recursive: true});
            }

            const logFile = path.join(logDir, 'format-errors.log');
            const logContent = failedFiles.map(({file, error}) =>
                `${file}: ${error}`
            ).join('\n');

            fs.writeFileSync(logFile, logContent, 'utf8');
            console.log(`\n\x1b[90m📄 详细错误日志已保存到: ${logFile}\x1b[0m`);
        }

        // 显示一些统计信息
        console.log('\n\x1b[36m📊 文件类型统计:\x1b[0m');
        const fileTypes = {};
        successFiles.forEach(file => {
            const ext = path.extname(file).toLowerCase();
            fileTypes[ext] = (fileTypes[ext] || 0) + 1;
        });

        Object.entries(fileTypes).forEach(([ext, count]) => {
            console.log(`  ${ext}: ${count} 个文件`);
        });

        if (failedFiles.length > 0) {
            console.log('\n\x1b[33m💡 建议:\x1b[0m');
            console.log('  1. 检查 .clang-format 文件是否存在');
            console.log('  2. 检查文件编码是否为 UTF-8');
            console.log('  3. 尝试手动格式化失败的文件:');
            console.log('     clang-format -i --style=file <文件名>');

            process.exit(1);
        } else {
            console.log('\n\x1b[32m🎉 所有文件格式化完成！\x1b[0m');
        }
    } catch (error) {
        console.error('\x1b[31m❌ 格式化失败:', error.message, '\x1b[0m');
        process.exit(1);
    }
}

// 执行格式化
formatCppFiles().catch(error => {
    console.error('\x1b[31m❌ 程序执行错误:', error.message, '\x1b[0m');
    process.exit(1);
});