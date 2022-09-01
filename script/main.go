package main

import (
	"fmt"
	"github.com/gocolly/colly"
	"io/ioutil"
	"os"
	"os/exec"
	"strings"
)

func main() {
	var hrefs []string
	c := colly.NewCollector(colly.UserAgent(`Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36`), colly.MaxDepth(1))

	// Find and visit all links
	c.OnHTML("div[class='ui fluid card project-card categorical-project-card']", func(e *colly.HTMLElement) {
		href, exists := e.DOM.Find(`a`).First().Attr(`href`)
		if !exists {
			return
		}

		hrefs = append(hrefs, href)
	})

	c.OnRequest(func(r *colly.Request) {
		fmt.Println("Visiting", r.URL)
	})

	c.OnResponse(func(r *colly.Response) {
		fmt.Println("Visited", r.Request.URL)
	})

	c.OnScraped(func(r *colly.Response) {
		gitee(hrefs)
		fmt.Println("Finished", r.Request.URL)
	})

	if err := c.Visit(`https://gitee.com/gvp/all`); nil != err {
		panic(fmt.Sprintf(`请求错误:%s`, err.Error()))
	}
}

func gitee(hrefs []string) {
	for _, href := range hrefs {
		dir := fmt.Sprintf(`gitee/%s`, dirName(href))
		// 不存在
		if _, err := os.Stat(dir); nil != err {
			if err := execCmd(fmt.Sprintf(`git clone https://gitee.com%s.git %s`, href, dir)); nil != err {
				fmt.Println(fmt.Sprintf(`处理git clone错误:%s`, err.Error()))
				return
			}
		}
		// 为空 表示处理完
		files, _ := getAllFiles(dir)
		if len(files) == 0 {
			continue
		}

		for _, f := range files {
			// 一次只处理一个文件
			filename, err := dealDvp(href, f)
			if err != nil {
				fmt.Println(fmt.Sprintf(`处理[%s]错误:%s`, f, err.Error()))
				return
			}

			commit := fmt.Sprintf(`%s %s`, href, filename)
			cmd := fmt.Sprintf(`git add . && git commit -m '%s' && git push origin main`, commit)
			if err := execCmd(cmd); nil != err {
				fmt.Println(fmt.Sprintf(`处理git提交错误:%s`, err.Error()))
			}
			return
		}
	}
}

func dealDvp(href, file string) (string, error) {
	fd := strings.Split(file, `/`)

	newFile := []string{href}
	for idx, f := range fd {
		if idx == 0 || idx == 1 || idx == len(fd)-1 {
			continue
		}
		newFile = append(newFile, f)
	}
	filename := fd[len(fd)-1:]
	newDir := strings.Join(newFile, `/`)

	if err := execCmd(fmt.Sprintf(`mkdir -p gvp%s`, newDir)); nil != err {
		return ``, err
	}
	if err := execCmd(fmt.Sprintf(`mv %s gvp%s/`, file, newDir)); nil != err {
		return ``, err
	}

	return strings.Join(filename, `/`), nil
}

//获取指定目录下的所有文件,包含子目录下的文件
func getAllFiles(dirPth string) (files []string, err error) {
	var dirs []string
	dir, err := ioutil.ReadDir(dirPth)
	if err != nil {
		return nil, err
	}
	PthSep := string(os.PathSeparator)
	for _, fi := range dir {
		if fi.IsDir() { // 目录, 递归遍历
			if fi.Name() == `.git` {
				continue
			}
			dirs = append(dirs, dirPth+PthSep+fi.Name())
			getAllFiles(dirPth + PthSep + fi.Name())
		} else {
			if fi.Name() == `.gitignore` || fi.Name() == `LICENSE` {
				continue
			}
			//// 过滤指定格式
			//ok := strings.HasSuffix(fi.Name(), ".go")
			//if ok {
			//	files = append(files, dirPth+PthSep+fi.Name())
			//}
			files = append(files, dirPth+PthSep+fi.Name())
		}
	}
	// 读取子目录下文件
	for _, table := range dirs {
		temp, _ := getAllFiles(table)
		for _, temp1 := range temp {
			files = append(files, temp1)
		}
	}
	return files, nil
}
func dirName(s string) string {
	dirs := strings.Split(s, `/`)

	str := ``
	for _, d := range dirs {
		if d != `` {
			str += fmt.Sprintf(`_%s`, d)
		}
	}
	return strings.Trim(str, `_`)
}

// execCmd xxx
func execCmd(c string) error {
	fmt.Println(`execCmd：`, c)
	cmd := exec.Command("/bin/bash", "-c", c)

	if err := cmd.Start(); nil != err {
		return err
	}

	if err := cmd.Wait(); nil != err {
		return err
	}

	return nil
}
