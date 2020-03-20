package com.algaworks.cobranca.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.Titulos;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	private static final String CadastroView = "CadastroTitulo";
	@Autowired
	private Titulos titulos;

	@RequestMapping("/novo")
	public ModelAndView Novo() {
		ModelAndView mv = new ModelAndView(CadastroView);
		mv.addObject("TodosStatusTitulo", StatusTitulo.values());
		mv.addObject(new Titulo());
		return mv;
	}

	// Salvar
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {

		if (errors.hasErrors()) {
			return CadastroView;
		}

		try {
			titulos.save(titulo);
			attributes.addFlashAttribute("mensagem", "Titulo salvo com sucesso");
			return "redirect:titulos/novo";
		} catch (DataIntegrityViolationException e) {
			errors.rejectValue("dataVencimento", null, "Formato de data inválido");
			return CadastroView;
		}
	}

	@RequestMapping
	public ModelAndView pesquisar() {
		List<Titulo> todosTitulos = titulos.findAll();
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("titulos", todosTitulos);
		return mv;

	}

	@RequestMapping(value = "{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CadastroView);
		mv.addObject(titulo);
		return mv;
	}

	@RequestMapping(path = "/excluir/{codigo}")
	public String excluir(@PathVariable("codigo") Titulo titulo) {
		this.titulos.delete(titulo);
		return "redirect:/titulos";
	}

	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable("codigo") Titulo titulo) {
		titulo.setStatus(StatusTitulo.RECEBIDO);
		titulos.save(titulo);
		return "OK";
	}

	@ModelAttribute("TodosStatusTitulo")
	public List<StatusTitulo> TodosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}

}
